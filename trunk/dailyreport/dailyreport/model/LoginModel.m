

#import "LoginModel.h"
#import "User.h"
#import "AppDelegate.h"

@implementation LoginModel

@synthesize user = _user;
@synthesize delegate = _delegate;
@synthesize checkSessionDelegate = _checkSessionDelegate;
@synthesize passwordDelegate = _passwordDelegate;
- (id)init
{
    if ((self = [super init])) {
        
        _user = [[User alloc] init];
    }
    return self;       
    
    
}

- (void)dealloc {
    TT_RELEASE_SAFELY(_user);
    TT_RELEASE_SAFELY(_delegate);
    TT_RELEASE_SAFELY(_checkSessionDelegate);
    TT_RELEASE_SAFELY(_passwordDelegate);
    [super dealloc];
}

- (BOOL)isOutdated {
    return NO;
}

- (void)login:(NSString *)username password:(NSString *)password{
    
    
    _user.username = username;
    _user.password = password;

    NSString* url= nil;
    if(DrAppDelegate.token!=nil){
        url = [BASE_URL stringByAppendingFormat:@"/login.do?userName=%@&password=%@&token=%@&version=%@",_user.username,_user.password,DrAppDelegate.token,VERSION];
    }else{
        url = [BASE_URL stringByAppendingFormat:@"/login.do?userName=%@&password=%@&version=%@",_user.username,_user.password,VERSION];
    }
    
    NSLog(@"URL:%@", url);
    
    TTURLRequest *request=[TTURLRequest requestWithURL:url delegate:self];
    request.timeoutInterval = 15;
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    
    TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
    request.response = response;
    TT_RELEASE_SAFELY(response);
    
    [request send];
}

- (void)checkSession{
  
    NSString* url= [BASE_URL stringByAppendingFormat:@"/checkSession.do?version=%@",VERSION];
    
    NSLog(@"URL:%@", url);
    
    TTURLRequest *request=[TTURLRequest requestWithURL:url delegate:self];
    request.timeoutInterval = 15;
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    
    TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
    request.response = response;
    TT_RELEASE_SAFELY(response);
    
    [request send];
}

- (void)changePassword:(NSString*)username password:(NSString*)password oldPassword:(NSString*)oldPassword{
    
    NSString* url= [BASE_URL stringByAppendingFormat:@"/changePassword.do?version=%@&userName=%@&password=%@&oldPassword=%@",VERSION,username,password,oldPassword];
    
    NSLog(@"URL:%@", url);
    
    TTURLRequest *request=[TTURLRequest requestWithURL:url delegate:self];
    request.timeoutInterval = 15;
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    
    TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
    request.response = response;
    TT_RELEASE_SAFELY(response);
    
    [request send];
}
#pragma mark -
#pragma mark TTURLRequestDelegate

- (void)requestDidStartLoad:(TTURLRequest*)request {
    if ([_delegate respondsToSelector:@selector(loginStart:)]) {
        [_delegate loginStart:_user];
    }   
    [super requestDidStartLoad:request];
}
- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {  
    if ([_delegate respondsToSelector:@selector(loginFailed:message:)]) {
        [_delegate loginFailed:error message:nil];
    }
    if ([_checkSessionDelegate respondsToSelector:@selector(checkSessionResult:)]) {
        [_checkSessionDelegate checkSessionResult:NO];
    }
    if ([_passwordDelegate respondsToSelector:@selector(passwordResult:success:)]) {
        [_passwordDelegate passwordResult:@"修改密码失败" success:NO];
    }
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    
    @try {
        TTURLJSONResponse* response = request.response;
        TTDASSERT([response.rootObject isKindOfClass:[NSDictionary class]]);
        NSDictionary* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        NSNumber* result = [data objectForKey:@"result"];
        if([result intValue]==0){
            if(_delegate!=nil){
                _user.sessionId = [data objectForKey:@"jsessionid"];
                _user.expiredDays = [data objectForKey:@"expiredDays"];
                NSNumber* timeoutInterval = [data objectForKey:@"timeoutInterval"];
                _user.timeoutInterval =timeoutInterval.intValue;
                _user.reset =((NSString*)[data objectForKey:@"reset"]).boolValue;
                if ([_delegate respondsToSelector:@selector(loginSuccess:)]) {
                    [_delegate loginSuccess:_user];
                }
            }else if(_checkSessionDelegate!=nil){
                if ([_checkSessionDelegate respondsToSelector:@selector(checkSessionResult:)]) {
                    [_checkSessionDelegate checkSessionResult:YES];
                }
            }else{
                [_passwordDelegate passwordResult:@"密码修改成功" success:YES];
            }
            
        }else{
            NSString* message =[data objectForKey:@"message"];
            if ([_delegate respondsToSelector:@selector(loginFailed:message:)]) {
                [_delegate loginFailed:nil message:message];
            }
            if ([_checkSessionDelegate respondsToSelector:@selector(checkSessionResult:)]) {
                [_checkSessionDelegate checkSessionResult:NO];
            }
            if ([_passwordDelegate respondsToSelector:@selector(passwordResult:success:)]) {
                [_passwordDelegate passwordResult:message success:NO];
            }
        }
    }
    @catch (NSException *exception) {
        if ([_delegate respondsToSelector:@selector(loginFailed:message:)]) {
            [_delegate loginFailed:nil message:[exception description]];
        } 
    }
    @finally {
        
    }
    [super requestDidFinishLoad:request];
    
    
    
    
}




@end
