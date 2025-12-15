import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { UsuarioRepresentation } from '../../models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
      return this.http.post<any>(`${this.baseUrl}/api/auth/login`, credentials)
        .pipe(
          tap(response => {
            // 1. Armazena o token na memória (preferencial) ou no localStorage
            localStorage.setItem('jwt_token', response.token);
          })
        );
    }

    getUsuarioLogado(): Observable<UsuarioRepresentation> {
      return this.http.get<UsuarioRepresentation>(`${this.baseUrl}/api/auth/me`);
    }

    logout(): void {
      localStorage.removeItem('jwt_token');
      // Redirecionar para a tela de login
    }

    isLoggedIn(): boolean {
      return !!localStorage.getItem('jwt_token');
    }

    getToken(): string | null {
      return localStorage.getItem('jwt_token');
    }
}
