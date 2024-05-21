import { Pool } from './pool.model';

export class Message {
  id: number;
  author: string;
  body: string;

  constructor(body: string){
    this.body = body;
  }
}