//
//  QQVideoRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-2-13.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQVideoRequestModel.h"
#import "Video.h"
#import "VideoItem.h"
#import "Constants.h"
@implementation QQVideoRequestModel

@synthesize delegate = _delegate;
@synthesize vid = _vid;

- (id)initWithVid:(NSString*)vid delegate:(id) delegate{
    if ((self = [super init])) {
        self.vid = vid;
        self.delegate = delegate;
    }
    return self;
}

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSString* url = [NSString stringWithFormat:QQ_VIDEO_URL,[self.vid substringToIndex:1],self.vid];
    
    NSLog(@"URL:%@", url);
    
    TTURLRequest* request = [TTURLRequest
                             requestWithURL: url
                             delegate: self];
    
    
    request.cachePolicy = TTURLRequestCachePolicyNone;
    
    
    TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
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
        
        TTURLJSONResponse* response = request.response;
        
        NSDictionary* data = response.rootObject;
        NSLog(@"response.rootObject:%@" ,data);
        NSDictionary* videoItems = [data objectForKey:@"videos"];
        
        Video* video = [[Video alloc] init];
        NSMutableArray* videoItemList = [NSMutableArray array];
        video.videoItemList = videoItemList;
        for(NSDictionary* entry in videoItems){
            VideoItem* videoItem = [[[VideoItem alloc] init] autorelease];
            videoItem.title = [entry objectForKey:@"tt"];
            videoItem.itemId = [entry objectForKey:@"vid"];
            videoItem.vid = self.vid;
            [videoItemList addObject:videoItem];
        }
        video.vid = self.vid;
        video.title = [data objectForKey:@"tt"];
        video.picUrl = [data objectForKey:@"pic"];
        video.actors = [[data valueForKey:@"actor"] componentsJoinedByString:@","];
        video.directors = [[data valueForKey:@"dctor"] componentsJoinedByString:@","];
        video.score = [data objectForKey:@"score"];
        video.area = [data objectForKey:@"area"];
        video.year = [data objectForKey:@"year"];
        video.description = [data objectForKey:@"desc"];
        [self.delegate videoDidFinishLoad:video];
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:NSLocalizedString(@"ERROROCCURED", @"Error  Occured"),[exception name]]);
        
    }
    
    [super requestDidFinishLoad:request];
}

- (void)requestDidStartLoad:(TTURLRequest*)request {
    NSLog(@"requestDidStartLoad:%@", request.urlPath);
    [self.delegate videoDidStartLoad:self.vid];
    [super requestDidStartLoad:request];
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [self.delegate video:self.vid didFailLoadWithError:error];
    [super request:request didFailLoadWithError:error];
}

@end
