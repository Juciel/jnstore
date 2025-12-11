import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { VendaInput, VendaRepresentation, VendaDetalheRepresentation, PageVendaRepresentation, ItemVendaProdutoRepresentation } from '../models';

interface VendaStats {
  total: number;
  variacao: number; // Variação percentual em relação ao período anterior
}

@Injectable({ providedIn: 'root' })
export class VendaService {
  private baseUrl = environment.apiUrl || 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  registrarVenda(input: VendaInput): Observable<VendaRepresentation> {
    return this.http.post<VendaRepresentation>(`${this.baseUrl}/vendas`, input);
  }

  listarVendas(): Observable<VendaRepresentation[]> {
    return this.http.get<VendaRepresentation[]>(`${this.baseUrl}/vendas`);
  }

  buscarPorId(id: number): Observable<VendaRepresentation> {
    return this.http.get<VendaRepresentation>(`${this.baseUrl}/vendas/${id}`);
  }

  detalharVenda(id: number): Observable<VendaDetalheRepresentation> {
    return this.http.get<VendaDetalheRepresentation>(`${this.baseUrl}/vendas/detalhes/${id}`);
  }

  deletarVenda(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/vendas/${id}`);
  }

  getVendasByCaixaId(caixaId: number): Observable<VendaRepresentation[]> {
    return this.http.get<VendaRepresentation[]>(`${this.baseUrl}/vendas/caixa/${caixaId}`);
  }

  getTopProdutosVendidos(limit: number = 5): Observable<ItemVendaProdutoRepresentation[]> {
    return this.http.get<ItemVendaProdutoRepresentation[]>(`${this.baseUrl}/vendas/produtos/top-vendidos?limit=${limit}`);
  }

  getVendasTotaisPorPeriodo(periodo: 'hoje' | 'semana' | 'mes' | 'ano'): Observable<VendaStats> {
    return this.http.get<VendaStats>(`${this.baseUrl}/vendas/totais?periodo=${periodo}`);
  }

  getNumeroVendasPorPeriodo(periodo: 'hoje' | 'semana' | 'mes' | 'ano'): Observable<VendaStats> {
    return this.http.get<VendaStats>(`${this.baseUrl}/vendas/quantidade?periodo=${periodo}`);
  }

  getTicketMedioPorPeriodo(periodo: 'hoje' | 'semana' | 'mes' | 'ano'): Observable<VendaStats> {
    return this.http.get<VendaStats>(`${this.baseUrl}/vendas/ticket-medio?periodo=${periodo}`);
  }

  getAllPaginado(page: number, size: number, sort: string[], dataInicial?: string, dataFinal?: string): Observable<PageVendaRepresentation> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (dataInicial) {
      params = params.set('dataInicial', dataInicial);
    }
    if (dataFinal) {
      params = params.set('dataFinal', dataFinal);
    }

    sort.forEach(s => {
      params = params.append('sort', s);
    });

    return this.http.get<PageVendaRepresentation>(`${this.baseUrl}/vendas/paginado`, { params });
  }
}
