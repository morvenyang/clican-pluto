//
//  KpiHistoryModel.m
//  HCMas
//
//  Created by zhang wei on 14-9-10.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "KpiHistoryModel.h"
#import "Constants.h"
#import "Kpi.h"
@implementation KpiHistoryModel
@synthesize delegate = _delegate;
- (id)init
{
    if ((self = [super init])) {
        
    }
    return self;
}
- (void)loadHistoryKpiByProjectId:(NSNumber*) projectId kpiType:(NSString*)kpiType pointName:(NSString*) pointName startDate:(NSDate*)startDate endDate:(NSDate*)endDate{
    NSString* url= nil;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    NSString* prefixUrl = [defaults valueForKey:BASE_URL_NAME];
    if (prefixUrl==nil||prefixUrl.length==0) {
        [_delegate loadKpiHistoryFailed:nil message:@"请先设置正确的服务器地址"];
    }
    NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
    [dateFormatter setDateFormat:@"yyyyMMdd"];
    url = [prefixUrl stringByAppendingFormat:@"/4.ashx?kpiType=%@&pointName=%@&startDate=%@&endDate=%@",kpiType,pointName,[dateFormatter stringFromDate:startDate],[dateFormatter stringFromDate:endDate]];
    
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
    [_delegate loadKpiHistoryStart];
    [super requestDidStartLoad:request];
}
- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    [_delegate loadKpiHistoryFailed:error message:nil];
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    @try {
        TTURLJSONResponse* response = request.response;
        NSArray* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);
        if(data.count>0){
            [_delegate loadKpiHistorySuccess:data];
        }else{
            [_delegate loadKpiHistoryFailed:nil message:@"没有相关历史数据"];
        }
    }
    @catch (NSException *exception) {
        [_delegate loadKpiHistoryFailed:nil message:[exception description]];
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
