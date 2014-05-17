

#import "LoginModel.h"
#import "User.h"
#import "AppDelegate.h"

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
    
    
    NSString* url= [BASE_URL stringByAppendingFormat:@"/login.do?userName=%@&password=%@",_user.username,_user.password];
    NSLog(@"URL:%@", url);
    
    TTURLRequest *request=[TTURLRequest requestWithURL:url delegate:self];
    request.timeoutInterval = 10;
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
        TTDASSERT([response.rootObject isKindOfClass:[NSDictionary class]]);
        NSDictionary* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        NSNumber* result = [data objectForKey:@"result"];
        if([result intValue]==0){
            _user.sessionId = [data objectForKey:@"jsessionid"];
            if ([_delegate respondsToSelector:@selector(loginSuccess:)]) {
                [_delegate loginSuccess:_user];
            }
        }else{
            NSString* message =[data objectForKey:@"message"];
            if ([_delegate respondsToSelector:@selector(loginFailed:message:)]) {
                [_delegate loginFailed:nil message:message];
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
