

#import "LoginModel.h"
#import "User.h"
#import "AppDelegate.h"
#import "Constants.h"
@implementation LoginModel

@synthesize user = _user;
@synthesize delegate = _delegate;

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
    [super dealloc];
}

- (BOOL)isOutdated {
    return NO;
}

- (void)login:(NSString *)username password:(NSString *)password{
    
    
    _user.username = username;
    _user.password = password;
    
    
    NSString* url= nil;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    NSString* prefixUrl = [defaults valueForKey:BASE_URL_NAME];
    if (prefixUrl==nil||prefixUrl.length==0) {
        [_delegate loginFailed:nil message:@"请先设置正确的服务器地址"];
    }
    if(HCMasAppDelegate.token!=nil){
        url = [prefixUrl stringByAppendingFormat:@"/1.ashx?userName=%@&passWord=%@&token=%@",_user.username,_user.password,HCMasAppDelegate.token];
    }else{
        url = [prefixUrl stringByAppendingFormat:@"/1.ashx?userName=%@&passWord=%@",_user.username,_user.password];
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
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    
    @try {
        TTURLJSONResponse* response = request.response;
        NSArray* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        
        if(data.count>0){
            NSDictionary* user = [data objectAtIndex:0];
            _user.sessionId = [user objectForKey:@"jsessionid"];
            if ([_delegate respondsToSelector:@selector(loginSuccess:)]) {
                [_delegate loginSuccess:_user];
            }
        }else{
            [_delegate loginFailed:nil message:@"用户名密码错误"];
        }
    }
    @catch (NSException *exception) {
        if ([_delegate respondsToSelector:@selector(loginFailed:message:)]) {
            [_delegate loginFailed:nil message:[exception description]];
        } 
    }
    @finally {
        [super requestDidFinishLoad:request];
    }
    
}
@end
