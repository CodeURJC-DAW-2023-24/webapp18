import { Pool } from './pool.model';

export interface Message {
  id?: number;
  pool: Pool;
  author: string;
  body: string;
  hasOwner: boolean;
}