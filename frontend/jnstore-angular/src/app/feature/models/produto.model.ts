import { CategoriaRepresetation } from './categoria.model';

export interface VariacaoProdutoRepresetation {
  id?: number;
  identificador?: string;
  cor?: string;
  tamanho?: string;
  quantidadeEstoque?: number;
}

export type GeneroRepresetation = 'MASCULINO' | 'FEMININO' | 'UNISSEX';

export interface ProdutoRepresetation {
  id?: number;
  nome?: string;
  descricao?: string;
  valorVenda?: number;
  valorCompra?: number;
  categoria?: CategoriaRepresetation;
  genero?: GeneroRepresetation;
  variacoes?: VariacaoProdutoRepresetation[];
  quantidadeTotalEstoque?: number;
  valorTotalCompra?: number;
  valorTotalVenda?: number;
  valorTotalLucro?: number;
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

export type FormaPagamento = 'CREDITO' | 'DINHEIRO' | 'PIX';

export interface PagamentoInput {
  forma: FormaPagamento;
  valorPago: number;
}

export interface PagamentoRepresentation {
  id?: number;
  forma?: FormaPagamento;
  valorPago?: number;
}

