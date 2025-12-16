import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PerfilRepresentation } from '../models';

@Injectable({ providedIn: 'root' })
export class PerfilService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  criar(perfil: PerfilRepresentation): Observable<PerfilRepresentation> {
      return this.http.post<PerfilRepresentation>(`${this.baseUrl}/api/profiles`, perfil);
  }

  podeEditarProduto(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeEditarProduto`);
  }

  podeVisualizarProduto(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeVisualizarProduto`);
  }

  podeVisualizarValorCompra(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeVisualizarValorCompra`);
  }

  podeVisualizarCategoria(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeVisualizarCategoria`);
  }

  podeEditarCategoria(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeEditarCategoria`);
  }

  podeAbrirCaixa(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeAbrirCaixa`);
  }

  podeVisualizarCaixa(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeVisualizarCaixa`);
  }

  podeFecharCaixa(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeFecharCaixa`);
  }

  podeRetirarCaixa(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeRetirarCaixa`);
  }

  podeEditarVenda(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeEditarVenda`);
  }

  podeVisualizarVenda(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeVisualizarVenda`);
  }

  podeEditarValorVenda(): Observable<boolean> {
      return this.http.get<boolean>(`${this.baseUrl}/api/profiles/podeEditarValorVenda`);
  }
}
