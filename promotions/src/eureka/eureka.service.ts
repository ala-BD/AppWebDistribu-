import { Injectable, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Eureka } from 'eureka-js-client';

@Injectable()
export class EurekaService implements OnModuleInit, OnModuleDestroy {
  private client: Eureka;

  onModuleInit() {
    this.client = new Eureka({
      instance: {
        app: 'promotions', // The name API Gateway and Eureka will use
        hostName: 'localhost',
        ipAddr: '127.0.0.1',
        port: {
          '$': 4005,
          '@enabled': true,
        },
        vipAddress: 'promotions',
        dataCenterInfo: {
          '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
          name: 'MyOwn', // Must be MyOwn for local environments
        },
      },
      eureka: {
        host: 'localhost',
        port: 8761,
        servicePath: '/eureka/apps/',
        maxRetries: 10,
        requestRetryDelay: 2000,
      },
    });

    this.client.start((error) => {
      if (error) {
        console.error('Erreur de connexion à Eureka =', error);
      } else {
        console.log('✅ Microservice Promotions enregistré sur Eureka avec succès !');
      }
    });
  }

  onModuleDestroy() {
    this.client.stop();
  }
}
