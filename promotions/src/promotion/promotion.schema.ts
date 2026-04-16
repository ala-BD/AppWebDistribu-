import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type PromotionDocument = Promotion & Document;

@Schema({ timestamps: true })
export class Promotion {
  // MongoDB uses _id by default, but we can serialize it as id if needed.
  
  @Prop({ required: true })
  produitId: number; // Mapped to Long in Java, number in TS

  @Prop({ required: true })
  pourcentageReduction: number; // Mapped to Double

  @Prop({ required: true })
  dateDebut: Date;

  @Prop({ required: true })
  dateFin: Date;
}

export const PromotionSchema = SchemaFactory.createForClass(Promotion);
