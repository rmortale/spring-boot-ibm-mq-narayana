package com.example.springbootibmmq.configuration;

import com.ibm.mq.jakarta.jms.MQXAConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.mq.spring.boot.MQConnectionFactoryCustomizer;
import com.ibm.mq.spring.boot.MQConnectionFactoryFactory;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jms.XAConnectionFactoryWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

@Configuration
@Slf4j
@EnableTransactionManagement
public class MqConfiguration {

    // ==================== ARTEMIS =========================
    @Bean
    @ConfigurationProperties("raifwd.artemis")
    public ArtemisProperties vaiProps() {
        return new ArtemisProperties();
    }

    @Bean
    ConnectionFactory vaiConnectionFactory(ArtemisProperties vaiProps,
                                           XAConnectionFactoryWrapper wrapper) throws Exception {
        ActiveMQXAConnectionFactory xacf = new ActiveMQXAConnectionFactory(
                vaiProps.getBrokerUrl(),
                vaiProps.getUser(),
                vaiProps.getPassword());
        return wrapper.wrapConnectionFactory(xacf);
    }

    @Bean
    public JmsListenerContainerFactory<?> vaiListenerContainerFactory(
            ConnectionFactory vaiConnectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, vaiConnectionFactory);
        return factory;
    }

    @Bean
    public JmsTemplate vaiJmsTemplate(ConnectionFactory vaiConnectionFactory) {
        return new JmsTemplate(vaiConnectionFactory);
    }


    //==================== IBM MQ =========================
    @Bean
    @ConfigurationProperties("app.raiqm")
    public MQConfigurationProperties raiConfigProperties() {
        return new MQConfigurationProperties();
    }

    @Bean
    public ConnectionFactory raiConnectionFactory(
            MQConfigurationProperties raiConfigProperties,
            ObjectProvider<List<MQConnectionFactoryCustomizer>> factoryCustomizers,
            XAConnectionFactoryWrapper wrapper) throws Exception {
        MQXAConnectionFactory xacf = new MQConnectionFactoryFactory(raiConfigProperties, factoryCustomizers.getIfAvailable())
                .createConnectionFactory(MQXAConnectionFactory.class);
        return wrapper.wrapConnectionFactory(xacf);
    }

    @Bean
    public JmsListenerContainerFactory<?> raiListenerContainerFactory(
            ConnectionFactory raiConnectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, raiConnectionFactory);
        return factory;
    }

    @Bean
    public JmsTemplate raiJmsTemplate(ConnectionFactory raiConnectionFactory) {
        return new JmsTemplate(raiConnectionFactory);
    }
}
