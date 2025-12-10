import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { CategoriaRepresetation } from '../models';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  listarTodas(): Observable<CategoriaRepresetation[]> {
    return this.http.get<CategoriaRepresetation[]>(`${this.baseUrl}/categorias`);
  }

  buscarPorId(id: number): Observable<CategoriaRepresetation> {
    return this.http.get<CategoriaRepresetation>(`${this.baseUrl}/categorias/${id}`);
  }

  criar(categoria: CategoriaRepresetation): Observable<CategoriaRepresetation> {
    return this.http.post<CategoriaRepresetation>(`${this.baseUrl}/categorias`, categoria);
  }

  atualizar(id: number, categoria: CategoriaRepresetation): Observable<CategoriaRepresetation> {
    return this.http.put<CategoriaRepresetation>(`${this.baseUrl}/categorias/${id}`, categoria);
  }

  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/categorias/${id}`);
  }
}
