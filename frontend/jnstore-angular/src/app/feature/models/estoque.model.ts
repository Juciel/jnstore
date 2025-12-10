export interface MovimentacaoEstoqueInput {
  idVariacao: number;
  quantidade: number;
  descricaoMotivo?: string;
  usuarioId?: number;
}

export interface MovimentacaoEstoqueRepresetation {
  id?: number;
  variacaoProduto?: any; // use VariacaoProdutoRepresetation from produto.model
  tipoMovimentacao?: 'ENTRADA' | 'SAIDA';
  quantidade?: number;
  dataMovimentacao?: string; // date-time
  descricaoMotivo?: string;
  usuarioId?: number;
}
