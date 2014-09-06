//
//  KpiModel.m
//  HCMas
//
//  Created by zhang wei on 14-9-6.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import "KpiModel.h"
#import "Constants.h"
@implementation KpiModel
@synthesize delegate = _delegate;
- (id)init
{
    if ((self = [super init])) {
        
    }
    return self;
}
- (void)loadKpiByProjectId:(NSNumber*) projectId{
    NSString* url= nil;
    NSUserDefaults* defaults = [NSUserDefaults standardUserDefaults];
    
    NSString* prefixUrl = [defaults valueForKey:BASE_URL_NAME];
    if (prefixUrl==nil||prefixUrl.length==0) {
        [_delegate loadKpiFailed:nil message:@"请先设置正确的服务器地址"];
    }
    
    url = [prefixUrl stringByAppendingFormat:@"/3.ashx?projectID=%@",projectId];
    
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
    [_delegate loadKpiStart];
    [super requestDidStartLoad:request];
}
- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    [_delegate loadKpiFailed:error message:nil];
    [super request:request didFailLoadWithError:error];
}


- (void)requestDidFinishLoad:(TTURLRequest*)request {
    @try {
        TTURLJSONResponse* response = request.response;
        NSArray* data = response.rootObject;
        NSLog(@"response.rootObject:%@",data);

        NSMutableDictionary* result = [NSMutableDictionary dictionary];
        if(data.count>0){
            NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
            [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
            
            NSArray* kpis = data;
            for(int i=1;i<kpis.count;i++){
                NSArray* kpiArray = [kpis objectAtIndex:i];
                NSMutableArray* resultArray = [NSMutableArray array];
                for(int j=0;j<kpiArray.count;j++){
                    NSDictionary* kpiDict = [kpiArray objectAtIndex:j];
                    NSString* type=[kpiDict objectForKey:@"__type"];
                    NSRange range = [type rangeOfString:@":"];
                    type = [type substringWithRange:NSMakeRange(0,range.location)];
                    if(j==0){
                        [result setValue:resultArray forKeyPath:type];
                    }
                    Kpi* kpi = [[[Kpi alloc] init] autorelease];
                    kpi.type = type;
                    kpi.pointName =[kpiDict objectForKey:@"pointName"];
                    kpi.v1 =[kpiDict objectForKey:@"v1"];
                    kpi.v2 =[kpiDict objectForKey:@"v2"];
                    kpi.v3 =[kpiDict objectForKey:@"v3"];
                    kpi.alertGrade =[kpiDict objectForKey:@"alertGrade"];
                    kpi.alertGrade_x =[kpiDict objectForKey:@"alertGrade_x"];
                    kpi.alertGrade_y =[kpiDict objectForKey:@"alertGrade_y"];
                    kpi.alertGrade_h =[kpiDict objectForKey:@"alertGrade_h"];
                    kpi.dis_x =[kpiDict objectForKey:@"dis_x"];
                    kpi.dis_y =[kpiDict objectForKey:@"dis_y"];
                    kpi.dis_h =[kpiDict objectForKey:@"dis_h"];
                    kpi.dacTime = [dateFormatter dateFromString:[kpiDict objectForKey:@"dacTime"]];
                    [resultArray addObject:kpi];
                }
            }
            
            [_delegate loadKpiSuccess:result];
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
