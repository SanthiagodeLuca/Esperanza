import { Injectable } from '@angular/core';
import { jwtDecode  } from 'jwt-decode';
@Injectable({
  providedIn: 'root'
})
export class TokenInformacionService {

  constructor() { }

  getUserIdFromToken(): number  {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = jwtDecode (token);
      const userId = decodedToken.userId;
      return userId; // "sub" es la propiedad donde generalmente se encuentra el ID del usuario en el token JWT
    }
    return 0;
  }
  geAuthoritiesFromToken(): string[]  {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedToken: any = jwtDecode (token);
      const authorities  = decodedToken.authorities;
      return authorities ; // "sub" es la propiedad donde generalmente se encuentra el ID del usuario en el token JWT
    }
    return [];
  }
}
