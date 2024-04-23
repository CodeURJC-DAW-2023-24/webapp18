import { Pool } from './pool.model';
import { Employer } from './employer.model';
import { Lifeguard } from './lifeguard.model';

export interface Offer {
  id?: number;
  pool: Pool;
  employer: string;
  lifeguard: Lifeguard;
  lifeguards: Lifeguard[];
  salary: string;
  start: string;
  type: string;
  description: string;
  poolName: string;
  poolID: number;
}