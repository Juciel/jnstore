import { CaixaInput, CaixaRepresentation } from './caixa.model';
import { PagamentoInput, PagamentoRepresentation } from './pagamento.model';

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

export interface VendaDetalheRepresentation {
  id?: number;
  caixa?: CaixaRepresentation;
  dataVenda?: string; // date-time
  totalBruto?: number;
  desconto?: number;
  totalLiquido?: number;
  itens?: ItemVendaProdutoRepresentation[];
  pagamentos?: PagamentoRepresentation[];
}

export interface ItemVendaInput {
  varianteId: number;
  precoUnitario: number;
  quantidade: number;
}

export interface ItemVendaRepresentation {
  id?: number;
  varianteId?: number;
  precoUnitario?: number;
  quantidade?: number;
}

export interface ItemVendaProdutoRepresentation {
  id?: number;
  idProduto?: number;
  nomeProduto?: string;
  descricaoProduto?: string;
  descricaoCategoria?: string;
  descricaoGenero?: string;
  varianteId?: number;
  identificador?: string;
  cor?: string;
  tamanho?: string;
  precoUnitario?: number;
  quantidade?: number;
}
