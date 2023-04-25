package shelter.backend.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextDelegateAware implements ApplicationContextAware {

    private ApplicationContext applicationContext;

public <T> T getService(String serviceComponent, Class<T> typeOfBean){
    return applicationContext.getBean(serviceComponent, typeOfBean);
}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
