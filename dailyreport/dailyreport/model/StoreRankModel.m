//
//  StoreRankModel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-26.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "StoreRankModel.h"
#import "Constants.h"
#import "AppDelegate.h"
#import "ChannelRank.h"

@implementation StoreRankModel

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
    
    NSString* url = [BASE_URL stringByAppendingFormat:@"/storeRank.do?brand=%@",[self.brand stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
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
                        NSArray* jsonChannels = [data objectForKey:@"channels"];
            NSMutableArray* channelRanks = [NSMutableArray array];
            for (NSDictionary* jsonChannel in jsonChannels) {
                ChannelRank* cr = [[[ChannelRank alloc] init] autorelease];
                [channelRanks addObject:cr];
                NSString* channel = [jsonChannel objectForKey:@"channel"];
                NSMutableArray* ranks = [NSMutableArray array];
                cr.ranks = ranks;
                cr.channel = channel;

                NSArray* jsonRanks = [jsonChannel objectForKey:@"ranks"];
                for(NSDictionary* jsonRank in jsonRanks){
                    Rank* r = [[[Rank alloc] init] autorelease];
                    r.name =[jsonRank objectForKey:@"name"];
                    r.dayAmount =[jsonRank objectForKey:@"amount"];
                    r.rate =[jsonRank objectForKey:@"rate"];
                    [ranks addObject:r];
                }
            }
            NSDate* date =  [dateFormatter dateFromString:[data objectForKey:@"date"]];
            [self.delegate brandDidFinishLoad:channelRanks date:date];
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
