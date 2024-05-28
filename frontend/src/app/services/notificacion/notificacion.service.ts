import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificacionService {
  private notificationsSubject = new Subject<string>();
  notifications$ = this.notificationsSubject.asObservable();

  notify(message: string) {
    this.notificationsSubject.next(message);
  }
}
