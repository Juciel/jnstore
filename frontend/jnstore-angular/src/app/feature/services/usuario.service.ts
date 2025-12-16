import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { UsuarioRepresentation, AtualizarSenhaRequest } from '../models';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  criar(usuario: UsuarioRepresentation): Observable<UsuarioRepresentation> {
    return this.http.post<UsuarioRepresentation>(`${this.baseUrl}/api/users/register`, usuario);
  }

  excluirUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/api/users/${id}`);
  }

  atualizarSenha(request: AtualizarSenhaRequest): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/api/users/atualizar-senha`, request);
  }

  isPrimeiroLogin(nomeUsuario: string): Observable<Boolean> {
    let params = new HttpParams().set('nomeUsuario', nomeUsuario);
    return this.http.get<Boolean>(`${this.baseUrl}/api/users/primeiro-login`, { params });
  }
}
