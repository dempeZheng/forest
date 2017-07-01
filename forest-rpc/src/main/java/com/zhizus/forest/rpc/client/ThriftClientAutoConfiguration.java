//package com.zhizus.forest.rpc.client;
//
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class ThriftClientAutoConfiguration implements BeanDefinitionRegistryPostProcessor, InitializingBean, EnvironmentAware {
//
//
////	private Logger logger = org.slf4j.LoggerFactory.getLogger(ThriftClientAutoConfiguration.class) ;
////
////	private static Properties properties = new Properties() ;
////
////	private static Set<String> beanNames = new HashSet<String>();
////
////	private RelaxedPropertyResolver propertyResolver ;
////
////	@Override
////	public void setEnvironment(Environment environment) {
////		this.propertyResolver = new RelaxedPropertyResolver(environment, "http.") ;
////	}
////
////
////	@Override
////	public void postProcessBeanFactory(
////			ConfigurableListableBeanFactory beanFactory)
////					throws BeansException {
////		beanFactory.addBeanPostProcessor(new ThriftClientBeanPostProcessor(beanFactory.getBean(ThriftClient.class) , beanFactory.getBean(Env.class)));
////	}
////
////	@Override
////	public void postProcessBeanDefinitionRegistry(
////			BeanDefinitionRegistry registry) throws BeansException {
////		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ThriftClient.class) ;
////		definitionBuilder.addPropertyReference("httpService", "httpService") ;
////		definitionBuilder.addPropertyValue("properties", properties) ;
////		registry.registerBeanDefinition("thriftClient", definitionBuilder.getBeanDefinition());
////
////		logger.info("register thrift client:{}" , beanNames );
////		for(String name : beanNames) {
////			String className = properties.getProperty(name + "." + "className") ;
////			try {
////				Class<?> clz = Class.forName(className + "$Client");
////				registerBean(registry , name , clz) ;
////			} catch (Exception e) {
////				logger.error("instance class error : className {}" , className , e);
////			}
////		}
////	}
////
////	private void registerBean(BeanDefinitionRegistry registry, String beanName, Class<?> beanClass) throws Exception{
////
////		GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
////		beanDefinition.setBeanClass(beanClass);
////
////
////
////		ConstructorArgumentValues argumentValues = new ConstructorArgumentValues();
////
////		TProtocol protocol = warpProtocol(beanName) ;
////
////		argumentValues.addGenericArgumentValue(protocol);
////
////		beanDefinition.setConstructorArgumentValues(argumentValues);
////
////		registry.registerBeanDefinition(beanName, beanDefinition);
////
////	}
////
////	private TProtocol warpProtocol(String beanName) throws TTransportException {
////		THttpClient transport = new THttpClient(properties.getProperty(beanName + ".url"));
////		TProtocol protocol = new TBinaryProtocol(transport);
////		return protocol;
////	}
////
////
////
////	private Set<String> getBeanNames(){
////		Set<String> sets = new HashSet<String>();
////		for(Object object : properties.keySet() ){
////			String key = (String)object ;
////			int index = key.indexOf(".") ;
////			if(index != -1){
////				sets.add(key.substring(0 , index)) ;
////			}
////		}
////		return sets ;
////	}
////
////	@Override
////	public void afterPropertiesSet() {
////		try {
////			properties.load(getClass().getClassLoader().getResourceAsStream("thrift.conf"));
////			beanNames = getBeanNames() ;
////		} catch (Exception e) {
////			logger.error("load thrift error ", e);
////		}
////	}
////
////
////	public  class ThriftClientBeanPostProcessor implements BeanPostProcessor{
////		private ThriftClient thriftClient ;
////
////
////
////		public ThriftClientBeanPostProcessor(ThriftClient thriftClient){
////			this.thriftClient = thriftClient ;
////		}
////
////		@Override
////		public Object postProcessBeforeInitialization(Object bean,
////				String beanName) throws BeansException {
////
////			if(beanNames.contains(beanName)){
////				ProxyFactoryBean proxyFactory = new ProxyFactoryBean() ;
////				proxyFactory.setTarget(bean);
////
////				proxyFactory.addAdvice(new MethodInterceptor() {
////					@Override
////					public Object invoke(MethodInvocation invocation) throws Throwable {
////
////						String methodName = invocation.getMethod().getName() ;
////						String className = properties.getProperty(beanName + ".className") ;
////
////						if("toString".equals(methodName)){
////							return invocation.proceed() ;
////						}
////
////						Transaction t = getClientCallTransaction(className , methodName) ;
////						try {
////							Object object = invocation.getMethod().invoke(thriftClient.get(beanName) , invocation.getArguments()) ;
////							t.setStatus(Transaction.SUCCESS);
////							return object ;
////						}catch(ServerException e){
////							//TODO
////							Cat.logEvent("RemoteServerError", "" + e.getCode());
////							if(e.getCode() == ServerException.QPS_LIMIT){
////								Cat.logEvent("qpsLimit", className + "." + methodName);
////							}
////							logError(invocation.getArguments(), t, e);
////							throw e  ;
////						}catch (Throwable e) {
////							logError(invocation.getArguments(), t, e);
////							throw e ;
////						}finally{
////							t.complete();
////						}
////					}
////				});
////				return proxyFactory.getObject() ;
////			}
////			return bean;
////		}
////
////		@Override
////		public Object postProcessAfterInitialization(Object bean,
////				String beanName) throws BeansException {
////			return bean;
////		}
////
////
////
////
////
////
////	}
//}
