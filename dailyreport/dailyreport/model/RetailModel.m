//
//  RetailModel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-21.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "RetailModel.h"
#import "Constants.h"
#import "AppDelegate.h"

@implementation RetailModel

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
    
    NSLog(@"load brand");
    
    NSString* url = [BASE_URL stringByAppendingFormat:@"/retail.do?brand=%@",[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
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
            NSMutableArray* channels = [NSMutableArray array];
            NSMutableArray* sorts = [NSMutableArray array];
            NSMutableArray* regions = [NSMutableArray array];
            NSArray* jsonChannels = [data objectForKey:@"channelRetail"];
            NSArray* jsonSorts = [data objectForKey:@"sortRetail"];
            NSArray* jsonRegions = [data objectForKey:@"regionRetail"];
            for (NSDictionary* jsonChannel in jsonChannels) {
                Retail* r = [[[Retail alloc] init] autorelease];
                r.name =[jsonChannel objectForKey:@"name"];
                r.dayAmount =[jsonChannel objectForKey:@"dayAmount"];
                [channels addObject:r];
            }
            for (NSDictionary* jsonSort in jsonSorts) {
                Retail* r = [[[Retail alloc] init] autorelease];
                r.name =[jsonSort objectForKey:@"name"];
                r.dayAmount =[jsonSort objectForKey:@"dayAmount"];
                [sorts addObject:r];
            }
            for (NSDictionary* jsonRegion in jsonRegions) {
                Retail* r = [[[Retail alloc] init] autorelease];
                r.name =[jsonRegion objectForKey:@"name"];
                r.dayAmount =[jsonRegion objectForKey:@"dayAmount"];
                [regions addObject:r];
            }
            NSDate* date =  [dateFormatter dateFromString:[data objectForKey:@"date"]];
            [self.delegate brandDidFinishLoad:channels sorts:sorts regions:regions date:date];
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
