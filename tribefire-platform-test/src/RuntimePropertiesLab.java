

import com.braintribe.model.processing.bootstrapping.TribefireRuntime;
import com.braintribe.utils.template.Template;

public class RuntimePropertiesLab {
    public static void main(String[] args) {
    	
    	
    	Template template1 = Template.parse("start-${foo}-$${egal}-end");
    	Template template2 = Template.parse("https://${foo}/?s=passwordInit&token=$${token}&email=$${email}&previousPwd=$${previousPwd}&claimId=$${claimId}");
    	
        System.setProperty("foo", "bar");
        TribefireRuntime.setProperty("key1", "start-${foo}-end");
        TribefireRuntime.setProperty("key2", "start-${foo}-$${egal}-end");
        TribefireRuntime.setProperty("key3", "start-${foo}-$${egal}-$${obsolete}-end");
        TribefireRuntime.setProperty("key4", "https://${foo}/?s=passwordInit&token=$${token}&email=$${email}&previousPwd=$${previousPwd}&claimId=$${claimId}");
        TribefireRuntime.setProperty("key5", "start-${foo}-${egal}-end");
        TribefireRuntime.setProperty("key6", "https://proventem-dev.tribefire-aws.cloud/?s=onboard&claimId=${claimId}");
        
        System.out.println(TribefireRuntime.getProperty("key0"));
        System.out.println(TribefireRuntime.getProperty("key1"));
        System.out.println(TribefireRuntime.getProperty("key2"));
        System.out.println(TribefireRuntime.getProperty("key3"));
        System.out.println(TribefireRuntime.getProperty("key4"));
        System.out.println(TribefireRuntime.getProperty("key5"));
        System.out.println(TribefireRuntime.getProperty("key6"));
    }
}