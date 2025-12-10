import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { MovimentacaoEstoqueInput, MovimentacaoEstoqueRepresetation } from '../models';

@Injectable({ providedIn: 'root' })
export class EstoqueService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registrarEntrada(input: MovimentacaoEstoqueInput): Observable<MovimentacaoEstoqueRepresetation> {
    return this.http.post<MovimentacaoEstoqueRepresetation>(`${this.baseUrl}/estoque/entrada`, input);
  }

  registrarSaida(input: MovimentacaoEstoqueInput): Observable<MovimentacaoEstoqueRepresetation> {
    return this.http.post<MovimentacaoEstoqueRepresetation>(`${this.baseUrl}/estoque/saida`, input);
  }
}
