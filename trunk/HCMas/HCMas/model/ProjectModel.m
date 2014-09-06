//
//  ProjectModel.m
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "ProjectModel.h"
#import "Constants.h"
@implementation ProjectModel
@synthesize delegate = _delegate;
- (id)init
{
    if ((self = [super init])) {
        
    }
    return self;
}

- (void)loadProjects{
    NSString* url= nil;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    NSString* prefixUrl = [defaults valueForKey:BASE_URL_NAME];
    if (prefixUrl==nil||prefixUrl.length==0) {
        [_delegate loadFailed:nil message:@"请先设置正确的服务器地址"];
    }
    
    url = [prefixUrl stringByAppendingFormat:@"/2.ashx"];
   
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
    [_delegate loadProjectStart];
    [super requestDidStartLoad:request];
}
- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    [_delegate loadProjectFailed:error message:nil];
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    @try {
        TTURLJSONResponse* response = request.response;
        NSArray* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        NSMutableArray* projects = [NSMutableArray array];
        if(data.count>0){
            for(NSDictionary* p in data){
                Project* project = [[[Project alloc] init] autorelease];
                project.projectId= [p valueForKey:@"id"];
                project.projectName = [p valueForKey:@"projectName"];
                [projects addObject:project];
                project.kpis = [p valueForKey:@"kpis"];
            }
            
            [_delegate loadProjectSuccess:projects];
        }
    }
    @catch (NSException *exception) {
        [_delegate loadProjectFailed:nil message:[exception description]];
    }
    @finally {
        [super requestDidFinishLoad:request];
    }
    
}
- (void)dealloc {
    TT_RELEASE_SAFELY(_delegate);
    [super dealloc];
}
@end
