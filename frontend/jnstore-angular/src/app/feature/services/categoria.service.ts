import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { PageCategoriaRepresentation, CategoriaRepresetation } from '../models';

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

  getAllPaginado(page: number, size: number, sort: string[], descricao: string): Observable<PageCategoriaRepresentation> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('descricao', descricao);

    sort.forEach(s => {
      params = params.append('sort', s);
    });

    return this.http.get<PageCategoriaRepresentation>(`${this.baseUrl}/categorias/paginado`, { params });
  }
}
