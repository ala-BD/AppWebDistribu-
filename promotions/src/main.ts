import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';

async function bootstrap() {
  const client = require('cloud-config-client');
  try {
    const config = await client.load({
      endpoint: 'http://localhost:8888',
      name: 'promotions',
      profiles: 'default',
    });
    
    // Inject variables from Config Server into process.env so Mongoose config can read them!
    process.env.MONGODB_URI = config.get('MONGODB_URI') || process.env.MONGODB_URI;
    
    const port = config.get('server.port') || 3000;

    const app = await NestFactory.create(AppModule);
    await app.listen(port);
    console.log(`🚀 Promotions Microservice running on port ${port} (Configured by Config Server)`);
  } catch (error) {
    console.error('Failed to load Config Server (http://localhost:8888). Using local fallback!', error);
    const app = await NestFactory.create(AppModule);
    await app.listen(4005);
  }
}
bootstrap();
