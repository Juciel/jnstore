export type FormaPagamento = 'CREDITO' | 'DEBITO' | 'DINHEIRO' | 'PIX';

export interface PagamentoInput {
  forma: FormaPagamento;
  valorPago: number;
  quantidadeParcelas: number;
}

export interface PagamentoRepresentation {
  id?: number;
  forma?: FormaPagamento;
  valorPago?: number;
  quantidadeParcelas: number;
}
