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

- (id)init{
    if ((self = [super init])) {
        self.brandList = [[NSMutableArray array] retain];
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_brandList);
    [super dealloc];
}
#pragma mark -
#pragma mark TTModel

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSLog(@"load index data");
    
    if (!self.isLoading) {
        NSString* url = [BASE_URL stringByAppendingString:@"/index.do"];
        
        NSLog(@"URL:%@", url);
        
        TTURLRequest* request = [TTURLRequest
                                 requestWithURL: url
                                 delegate: self];
        
        [request setValue:[@"JSESSIONID=" stringByAppendingString:DrAppDelegate.user.sessionId]forHTTPHeaderField:@"Cookie"];
        
        request.cachePolicy = cachePolicy;
        //request.cacheExpirationAge = TT_CACHE_EXPIRATION_AGE_NEVER;
        
        TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
        request.response = response;
        TT_RELEASE_SAFELY(response);
        
        [request send];
        
        //}
    }
    
}
#pragma mark -
#pragma mark TTURLRequestDelegate

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    
    @try {
        
        TTURLJSONResponse* response = request.response;
        TTDASSERT([response.rootObject isKindOfClass:[NSDictionary class]]);
        
        NSDictionary* data = response.rootObject;
        
        NSLog(@"response.rootObject:%@" ,data);
        NSNumber* result = [data objectForKey:@"result"];
        if([result intValue]==0){
            NSArray* brands = [data objectForKey:@"brands"];
            for (NSDictionary* brandDict in brands) {
                Brand* brand = [[Brand alloc] init];
                brand.dayAmount = [brandDict objectForKey:@"dayAmount"];
                brand.brand =[brandDict objectForKey:@"brand"];
                [_brandList addObject:brand];
            }
            [super requestDidFinishLoad:request];
        }else{
            TTAlert([data objectForKey:@"message"]);
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
