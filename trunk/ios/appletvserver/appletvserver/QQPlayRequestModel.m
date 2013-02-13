//
//  QQPlayRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//
#import "QQPlayRequestModel.h"
#import "Constants.h"

@implementation QQPlayRequestModel

@synthesize videoItemId=_videoItemId;
@synthesize delegate=_delegate;
- (id)initWithVideoItemId:(NSString*)videoItemId delegate:(id)delegate{
    if ((self = [super init])) {
        self.videoItemId = videoItemId;
        self.delegate = delegate;
        
    }
    return self;
}

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSString* url = [NSString stringWithFormat:QQ_PLAY_API,self.videoItemId];
    
    NSLog(@"URL:%@", url);
    
    TTURLRequest* request = [TTURLRequest
                             requestWithURL: url
                             delegate: self];
    
    
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    
    TTURLDataResponse* response = [[TTURLDataResponse alloc] init];
    request.response = response;
    TT_RELEASE_SAFELY(response);
    
    [request send];
    
    
    
}

- (BOOL)isOutdated {
    return NO;
}

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    
    @try {
        
        TTURLDataResponse* response = request.response;
        NSString* content = [[[NSString alloc] initWithData:[response data] encoding:NSUTF8StringEncoding] autorelease];
        
        NSRange startRange = [content rangeOfString: @"\"url\":\""];
        content = [content substringFromIndex:startRange.location+startRange.length];
        NSRange endRange = [content rangeOfString: @"\""];
        content = [content substringToIndex:endRange.location];
        VideoItem* videoItem = [[[VideoItem alloc] init] autorelease];
        videoItem.itemId = self.videoItemId;
        videoItem.mediaUrl = content;
        NSLog(@"mediaUrl:%@",videoItem.mediaUrl);
        [self.delegate videoItemDidFinishLoad:videoItem];
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:@"Error Occured:%@",[exception description]]);
        
    }
    
    [super requestDidFinishLoad:request];
}

- (void)requestDidStartLoad:(TTURLRequest*)request {
    NSLog(@"requestDidStartLoad:%@", request.urlPath);
    [self.delegate videoItemDidStartLoad:self.videoItemId];
    [super requestDidStartLoad:request];
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [self.delegate videoItem:self.videoItemId didFailLoadWithError:error];
    [super request:request didFailLoadWithError:error];
}
@end
