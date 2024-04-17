import { Person } from './person.model';
import { Pool } from './pool.model';
import { Offer } from './offer.model';

export interface Lifeguard extends Person {
  photo?: Blob;
  imageUser?: boolean;
  document?: string;
  skills?: string[];
  pools: Pool[];
  offers_accepted: Offer[];
  offers: Offer[];
  offerAssigned: boolean;
}