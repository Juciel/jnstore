export interface CaixaInput {
  valor?: number;
}

export interface CaixaRepresentation {
  id?: number;
  idUsuarioAbertura?: number;
  dataAbertura?: string; // date-time
  valorInicial?: number;
  idUsuarioFechamento?: number;
  dataFechamento?: string; // date-time
  valorFinal?: number;
  idUsuarioRetirada?: number;
  dataRetirada?: string; // date-time
  valorRetirada?: number;
  status: StatusCaixa;
}

export type StatusCaixa = 'ABERTO' | 'FECHADO' | 'RETIRADO';
