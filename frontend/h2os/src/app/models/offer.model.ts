import { Pool } from './pool.model';
import { Employer } from './employer.model';
import { Lifeguard } from './lifeguard.model';

export class Offer {
  id?: number;
  pool?: Pool;
  direction?: String;
  employer?: string;
  lifeguard?: Lifeguard;
  lifeguards?: Lifeguard[];
  salary?: string;
  start?: string;
  type?: string;
  description?: string;
  poolName?: string;
  poolID?: number;
  image: string;

  constructor() {
    this.id = undefined;
    this.pool = undefined;
    this.direction = '';
    this.employer = '';
    this.lifeguard = undefined;
    this.lifeguards = [];
    this.salary = '';
    this.start = '';
    this.type = '';
    this.description = '';
    this.poolID = 0;
}
}
