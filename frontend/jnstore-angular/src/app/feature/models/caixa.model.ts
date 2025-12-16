export interface CaixaInput {
  valor?: number;
}

export interface CaixaRepresentation {
  id?: number;
  usuarioAbertura?: string;
  dataAbertura?: string; // date-time
  valorInicial?: number;
  usuarioFechamento?: string;
  dataFechamento?: string; // date-time
  valorFinal?: number;
  usuarioRetirada?: string;
  dataRetirada?: string; // date-time
  valorRetirada?: number;
  status: StatusCaixa;
}

export type StatusCaixa = 'ABERTO' | 'FECHADO' | 'RETIRADO';
