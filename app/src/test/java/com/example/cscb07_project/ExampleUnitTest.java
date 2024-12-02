
package com.example.cscb07_project;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Mock
    LogInActivity activity;

    @Mock
    LogInModel model;


    @Test
    public void checkEmptyCredential(){
        LogInPresenter logInPresenter=new LogInPresenter(activity, model);
        logInPresenter.validateCredentials("","TestPass1234");
        verify(activity).setEmailError("Email cannot be empty");
    }

    @Test
    public void checkEmptyPassword(){
        LogInPresenter logInPresenter=new LogInPresenter(activity,model);
        logInPresenter.validateCredentials("@example.com","");
        verify(activity).setPasswordError("Password cannot be empty");
    }

    @Test
    public void checkLoginSuccess(){
        LogInPresenter logInPresenter=new LogInPresenter(activity,model);
        String email = "test@example.com";
        String password = "BeagleTest_001";
        logInPresenter.validateCredentials(email,password);
        ArgumentCaptor<LogInModel.OnLoginFinishedListener> captor =
                ArgumentCaptor.forClass(LogInModel.OnLoginFinishedListener.class);
        verify(model).login(eq(email), eq(password), captor.capture());
        captor.getValue().onSuccess();

        verify(activity).navigateToHome();
    }

    @Test
    public void checkLoginError(){
        LogInPresenter logInPresenter=new LogInPresenter(activity,model);
        String email = "test@example.com";
        String password = "BeagleTest_001";
        String errorMessage="Invalid credential";
        logInPresenter.validateCredentials(email,password);
        ArgumentCaptor<LogInModel.OnLoginFinishedListener> captor =
                ArgumentCaptor.forClass(LogInModel.OnLoginFinishedListener.class);
        verify(model).login(eq(email), eq(password), captor.capture());
        captor.getValue().onError(errorMessage);
        verify(activity).setEmailError(errorMessage);
    }

}