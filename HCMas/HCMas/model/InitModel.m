//
//  InitModel.m
//  HCMas
//
//  Created by zhang wei on 14-11-21.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "InitModel.h"
#import "AppDelegate.h"
#import "Constants.h"
#import "Project.h"
@implementation InitModel
@synthesize delegate = _delegate;

- (id)init
{
    if ((self = [super init])) {
        

    }
    return self;
    
    
}

- (void)dealloc {

    TT_RELEASE_SAFELY(_delegate);
    [super dealloc];
}

- (BOOL)isOutdated {
    return NO;
}

- (void)loadInitData{

    NSString* url= INIT_URL;

    
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
    if ([_delegate respondsToSelector:@selector(initStart)]) {
        [_delegate initStart];
    }
    [super requestDidStartLoad:request];
}
- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    if ([_delegate respondsToSelector:@selector(initFailed:message:)]) {
        [_delegate initFailed:error message:nil];
    }
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    
    @try {
        TTURLJSONResponse* response = request.response;
        NSArray* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        NSMutableArray* result = [NSMutableArray array];
        if(data.count>0){
            for(NSDictionary* d in data){
                Project * p = [[[Project alloc] init] autorelease];
                p.projectName = [d objectForKey:@"projectName"];
                p.serverConfig = ((NSString*)[d objectForKey:@"serverConfig"]).boolValue;
                p.serverUrl = [d objectForKey:@"serverUrl"];
                p.kpis = [d valueForKey:@"kpis"];
                p.kpiNames =[d valueForKey:@"kpiNames"];
                [result addObject:p];
            }
            if ([_delegate respondsToSelector:@selector(initSuccess:)]) {
                [_delegate initSuccess:result];
            }
        }else{
            [_delegate initFailed:nil message:@"服务器端初始化数据配置错误"];
        }
    }
    @catch (NSException *exception) {
        if ([_delegate respondsToSelector:@selector(initFailed:message:)]) {
            [_delegate initFailed:nil message:[exception description]];
        }
    }
    @finally {
        [super requestDidFinishLoad:request];
    }
    
}

@end
