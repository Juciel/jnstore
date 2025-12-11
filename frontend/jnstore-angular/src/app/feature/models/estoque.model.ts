export interface MovimentacaoEstoqueInput {
  idVariacao: number;
  quantidade: number;
  descricaoMotivo?: string;
}

export interface MovimentacaoEstoqueRepresetation {
  id?: number;
  variacaoProduto?: any; // use VariacaoProdutoRepresetation from produto.model
  tipoMovimentacao?: 'ENTRADA' | 'SAIDA';
  quantidade?: number;
  dataMovimentacao?: string; // date-time
  descricaoMotivo?: string;
}
