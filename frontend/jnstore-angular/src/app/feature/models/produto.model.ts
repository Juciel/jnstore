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
  valorCompra?: number;
  valorVenda?: number;
  valorVendaPix?: number;
  valorVendaCredito?: number;
  categoria?: CategoriaRepresetation;
  genero?: GeneroRepresetation;
  variacoes?: VariacaoProdutoRepresetation[];
  quantidadeTotalEstoque?: number;
  valorTotalCompra?: number;
  valorTotalVenda?: number;
  valorTotalLucro?: number;
}
