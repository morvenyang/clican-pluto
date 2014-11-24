//
//  StoreSumModel.m
//  dailyreport
//
//  Created by zhang wei on 14-11-24.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "StoreSumModel.h"
#import "Constants.h"
#import "AppDelegate.h"

@implementation StoreSumModel

@synthesize brand = _brand;
@synthesize delegate     = _delegate;

- (id)initWithBrand:(NSString*)brand delegate:(id) delegate{
    if ((self = [super init])) {
        self.brand = brand;
        self.delegate = delegate;
    }
    return self;
}


- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSLog(@"load store sum");
    
    NSString* url = [BASE_URL stringByAppendingFormat:@"/storeSum.do?brand=%@&version=%@",[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding],VERSION];
    if(DrAppDelegate.user.date!=nil){
        NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyyMMdd"];
        NSString* strDate = [dateFormatter stringFromDate:DrAppDelegate.user.date];
        url = [url stringByAppendingFormat:@"&date=%@",strDate];
    }
    NSLog(@"jsessionid=%@",DrAppDelegate.user.sessionId);
    NSLog(@"URL:%@", url);
    
    TTURLRequest* request = [TTURLRequest
                             requestWithURL: url
                             delegate: self];
    request.timeoutInterval = DrAppDelegate.user.timeoutInterval;
    [request setValue:[@"JSESSIONID=" stringByAppendingString:DrAppDelegate.user.sessionId]forHTTPHeaderField:@"Cookie"];
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
    request.response = response;
    TT_RELEASE_SAFELY(response);
    
    [request send];
    
    
    
}

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    
    @try {
        
        TTURLJSONResponse* response = request.response;
        TTDASSERT([response.rootObject isKindOfClass:[NSDictionary class]]);
        
        NSDictionary* data = response.rootObject;
        NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        NSLog(@"response.rootObject:%@" ,data);
        NSNumber* result = [data objectForKey:@"result"];
        if([result intValue]==0){
            NSArray* monthlySums = [data objectForKey:@"monthlySums"];
            NSArray* yearlySums = [data objectForKey:@"monthlySums"];
            NSMutableArray* ms = [NSMutableArray array];
            NSMutableArray* ys = [NSMutableArray array];
            for (NSDictionary* m in monthlySums) {
                StoreSum* s = [[[StoreSum alloc] init] autorelease];
                [ms addObject:s];
                s.sumType = [m objectForKey:@"sumType"];
                s.total =[m objectForKey:@"total"];
                s.selfV =[m objectForKey:@"self"];
                s.unionV=[m objectForKey:@"union"];
                s.join =[m objectForKey:@"join"];
            }
            for (NSDictionary* y in yearlySums) {
                StoreSum* s = [[[StoreSum alloc] init] autorelease];
                [ys addObject:s];
                s.sumType = [y objectForKey:@"sumType"];
                s.total =[y objectForKey:@"total"];
                s.selfV =[y objectForKey:@"self"];
                s.unionV=[y objectForKey:@"union"];
                s.join =[y objectForKey:@"join"];
            }
            NSDate* date =  [dateFormatter dateFromString:[data objectForKey:@"date"]];
            [self.delegate brandDidFinishLoad:ms yearlySums:ys date:date];
        }else{
            if(result.intValue==1002){
                //not login
                [self tryAutoLogin];
            }else{
                TTAlert([data objectForKey:@"message"]);
            }
        }
        
        
        
        
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:NSLocalizedString(@"ERROROCCURED", @"Error  Occured"),[exception name]]);
        
    }
    
    [super requestDidFinishLoad:request];
}

- (void)requestDidStartLoad:(TTURLRequest*)request {
    NSLog(@"requestDidStartLoad:%@", request.urlPath);
    [self.delegate brandDidStartLoad:self.brand];
    [super requestDidStartLoad:request];
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [self.delegate brand:self.brand didFailLoadWithError:error];
    [super request:request didFailLoadWithError:error];
}


- (void) dealloc {
    TT_RELEASE_SAFELY(_brand);
    TT_RELEASE_SAFELY(_delegate);
    [super dealloc];
}


@end
