import { Injectable, NotFoundException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Promotion, PromotionDocument } from './promotion.schema';

@Injectable()
export class PromotionService {
  constructor(
    @InjectModel(Promotion.name) private promotionModel: Model<PromotionDocument>,
  ) {}

  async create(createPromotionDto: any): Promise<Promotion> {
    const createdPromotion = new this.promotionModel(createPromotionDto);
    return createdPromotion.save();
  }

  async findAll(): Promise<Promotion[]> {
    return this.promotionModel.find().exec();
  }

  async findOne(id: string): Promise<Promotion> {
    const promotion = await this.promotionModel.findById(id).exec();
    if (!promotion) {
      throw new NotFoundException(`Promotion with ID ${id} not found`);
    }
    return promotion;
  }

  async update(id: string, updatePromotionDto: any): Promise<Promotion> {
    const existingPromotion = await this.promotionModel.findByIdAndUpdate(
      id,
      updatePromotionDto,
      { new: true },
    ).exec();
    
    if (!existingPromotion) {
      throw new NotFoundException(`Promotion with ID ${id} not found`);
    }
    return existingPromotion;
  }

  async remove(id: string): Promise<any> {
    const deletedPromotion = await this.promotionModel.findByIdAndDelete(id).exec();
    if (!deletedPromotion) {
      throw new NotFoundException(`Promotion with ID ${id} not found`);
    }
    return deletedPromotion;
  }
}
