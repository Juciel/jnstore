export type NOME_TAXA = 'CREDITO' | 'DEBITO' | 'DINHEIRO' | 'PIX';

export interface TaxaInput {
  nome: string;
  valorTaxa: number;
}

export interface TaxaRepresentation {
  id?: number;
  nome: string;
  valorTaxa: number;
}
