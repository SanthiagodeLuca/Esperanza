import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from 'src/app/modelos/user';
import { UserService } from '../user/user.service';
import { TokenInformacionService } from '../token/token-informacion.service';

@Injectable({
  providedIn: 'root'
})
export class UserRoleService {
  private currentUserSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  public currentUser$: Observable<User | null> = this.currentUserSubject.asObservable();
  private errorMessage: string | null = null;

  constructor(private userService: UserService, private tokenInformacionService: TokenInformacionService) {
    this.initializeCurrentUser();
  }

  private initializeCurrentUser(): void {
    const userId = this.tokenInformacionService.getUserIdFromToken();
    if (userId) {
      this.userService.getUser(userId).subscribe({
        next: (userData) => {
          this.currentUserSubject.next(userData);
        },
        error: (errorData) => {
          this.errorMessage = errorData;
          console.error('Error fetching user data:', errorData);
        },
        complete: () => {
          console.info('User data fetched successfully');
        }
      });
    }
  }

  getCurrentUserRole(): string | null {
    const currentUser = this.currentUserSubject.getValue();
    return currentUser && currentUser.role !== undefined ? currentUser.role : null;
  }

  isUserRole(role: string): boolean {
    const currentUser = this.currentUserSubject.getValue();
    return currentUser ? currentUser.role === role : false;
  }
}
