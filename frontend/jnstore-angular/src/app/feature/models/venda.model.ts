import { CaixaInput, CaixaRepresentation } from './caixa.model';
import { ItemVendaInput, ItemVendaRepresentation, PagamentoInput, PagamentoRepresentation } from './produto.model';

export interface VendaInput {
  desconto?: number;
  itens: ItemVendaInput[];
  totalBruto?: number;
  pagamentos: PagamentoInput[];
  totalLiquido?: number;
}

export interface VendaRepresentation {
  id?: number;
  caixaId?: number;
  dataVenda?: string; // date-time
  totalBruto?: number;
  desconto?: number;
  totalLiquido?: number;
  itens?: ItemVendaRepresentation[];
  pagamentos?: PagamentoRepresentation[];
}
