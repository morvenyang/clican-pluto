//
//  BrandModel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "BrandModel.h"
#import "Constants.h"
#import "AppDelegate.h"

@implementation BrandModel
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
    
    NSString* url = [BASE_URL stringByAppendingFormat:@"/brand.do?brand=%@",[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
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
            //基础的品牌数据
            NSDictionary* jsonBrand = [data objectForKey:@"brandResult"];
            Brand* brand = [[[Brand alloc] init] autorelease];
            brand.brand = [jsonBrand objectForKey:@"brand"];
            brand.dayAmount = [jsonBrand objectForKey:@"dayAmount"];
            brand.weekAmount = [jsonBrand objectForKey:@"weekAmount"];
            brand.yearAmount = [jsonBrand objectForKey:@"yearAmount"];
            brand.dayLike = [jsonBrand objectForKey:@"dayLike"];
            brand.weekLike = [jsonBrand objectForKey:@"weekLike"];
            brand.yearLike = [jsonBrand objectForKey:@"yearLike"];
            NSLog(@"%@",[jsonBrand objectForKey:@"date"]);
            brand.date = [dateFormatter dateFromString:[jsonBrand objectForKey:@"date"]];
            //该品牌本周的数据
            NSMutableArray* weeks = [NSMutableArray array];
            NSArray* jsonWeeks = [data objectForKey:@"weeks"];
            for (NSDictionary* jsonWeek in jsonWeeks) {
                Brand* weekBrand = [[[Brand alloc] init]autorelease];
                weekBrand.date = [dateFormatter dateFromString:[jsonWeek objectForKey:@"date"]];
                NSString* da =[jsonWeek objectForKey:@"dayAmount"];
                if(da!=nil&&da.length>0){
                    weekBrand.dayAmount = [jsonWeek objectForKey:@"dayAmount"];
                }else{
                    weekBrand.dayAmount = nil;
                }
                [weeks addObject:weekBrand];
            }
            
            //该品牌渠道的数据
            NSMutableArray* channels = [NSMutableArray array];
            NSArray* jsonChannels = [data objectForKey:@"channels"];
            for (NSDictionary* jsonChannel in jsonChannels) {
                Channel* channel = [[[Channel alloc] init] autorelease];
                channel.channel = [jsonChannel objectForKey:@"channel"];
                channel.dayAmount =[jsonChannel objectForKey:@"dayAmount"];
                [channels addObject:channel];
            }
            [self.delegate brandDidFinishLoad:brand channels:channels weeks:weeks];
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
