//
//  IndexListModel.m
//  dailyreport
//
//  Created by zhang wei on 14-5-17.
//  Copyright (c) 2014年 Peace Bird. All rights reserved.
//

#import "IndexListModel.h"
#import "AppDelegate.h"
#import "Brand.h"
@implementation IndexListModel
@synthesize brandList = _brandList;
@synthesize yesterday = _yesterday;
@synthesize date = _date;
- (id)init{
    if ((self = [super init])) {
        self.brandList = [NSMutableArray array];
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_brandList);
    TT_RELEASE_SAFELY(_date);
    [super dealloc];
}
#pragma mark -
#pragma mark TTModel

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSLog(@"load index data");
    

    NSString* url = [BASE_URL stringByAppendingFormat:@"/index.do?version=%@",VERSION];

    if(DrAppDelegate.user.date!=nil){
        NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyyMMdd"];
        NSString* strDate = [dateFormatter stringFromDate:DrAppDelegate.user.date];
        url = [url stringByAppendingFormat:@"&date=%@",strDate];
    }
        NSLog(@"URL:%@", url);
        
        TTURLRequest* request = [TTURLRequest
                                 requestWithURL: url
                                 delegate: self];
        request.timeoutInterval = DrAppDelegate.user.timeoutInterval;
        if(DrAppDelegate.user.sessionId==nil||DrAppDelegate.user.sessionId.length==0){
            [self tryAutoLogin];
            return;
        }
        [request setValue:[@"JSESSIONID=" stringByAppendingString:DrAppDelegate.user.sessionId]forHTTPHeaderField:@"Cookie"];
        
        request.cachePolicy = TTURLRequestCachePolicyNone;
        //request.cacheExpirationAge = TT_CACHE_EXPIRATION_AGE_NEVER;
        
        TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
        request.response = response;
        TT_RELEASE_SAFELY(response);
        
        [request send];
        

    
}
#pragma mark -
#pragma mark TTURLRequestDelegate

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    
    @try {
        [_brandList removeAllObjects];
        TTURLJSONResponse* response = request.response;
        TTDASSERT([response.rootObject isKindOfClass:[NSDictionary class]]);
        
        NSDictionary* data = response.rootObject;
        
        NSLog(@"response.rootObject:%@" ,data);
        NSNumber* result = [data objectForKey:@"result"];
        
        if([result intValue]==0){
            self.yesterday = [[data objectForKey:@"yesterday"] boolValue];
            NSDateFormatter* dateFormatter = [[[NSDateFormatter alloc] init] autorelease];
            [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
            [dateFormatter setDateFormat:@"yyyy-MM-dd"];
            NSArray* avaliableDates=[data objectForKey:@"availableDates"];
            NSMutableArray* ads = [NSMutableArray array];
            for(NSString* avaliableDate in avaliableDates){
                NSDate* d =[dateFormatter dateFromString:avaliableDate];
                [ads addObject:d];
            }
            DrAppDelegate.user.availableDates = ads;
            self.date = [dateFormatter dateFromString:[data objectForKey:@"date"]];
            NSArray* brands = [data objectForKey:@"brands"];
            for (NSDictionary* brandDict in brands) {
                Brand* brand = [[Brand alloc] init];
                brand.dayAmount = [brandDict objectForKey:@"dayAmount"];
                brand.brand =[brandDict objectForKey:@"brand"];
                [_brandList addObject:brand];
            }
            
            [_brandList sortUsingSelector:@selector(compare:)];
            IndexViewController* index = (IndexViewController*)[TTNavigator navigator].visibleViewController;
            [index updateDate];
            [super requestDidFinishLoad:request];
        }else{
            if(result.intValue==1002){
                //not login
                [self tryAutoLogin];
            }else{
                DrAppDelegate.user.date = DrAppDelegate.user.oldDate;
                IndexViewController* index = (IndexViewController*)[TTNavigator navigator].visibleViewController;
                [index updateDate];
                TTAlert([data objectForKey:@"message"]);
            }
        }
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:NSLocalizedString(@"ERROROCCURED", @"Error  Occured"),[exception name]]);
        
    }
    
    
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [super request:request didFailLoadWithError:error];
    
}

@end
