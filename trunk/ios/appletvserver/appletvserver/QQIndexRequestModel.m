//
//  QQIndexRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-2-12.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#import "QQIndexRequestModel.h"
#import "Constants.h"
#import "Video.h"
#import "RemoveCallbackURLJSONResponse.h"

@implementation QQIndexRequestModel

@synthesize qqChannel = _qqChannel;
@synthesize keyword = _keyword;
@synthesize videoList = _videoList;
@synthesize finished = _finished;

- (id)initWithQQChannel:(QQChannel)channel{
    if ((self = [super init])) {
        self.qqChannel = channel;
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_keyword);
    TT_RELEASE_SAFELY(_videoList);
    [super dealloc];
}

- (void)load:(TTURLRequestCachePolicy)cachePolicy more:(BOOL)more
{
    
    NSLog(@"load");
    
    if (!self.isLoading /*&& TTIsStringWithAnyText(_searchQuery)*/) {
        if (more) {
            _page++;
        }
        else {
            _page = 0;
            _finished = NO;
            [_videoList removeAllObjects];
        }
        
        NSString* url =[@"" stringByAppendingFormat:QQ_CHANNEL_URL,_page,self.qqChannel,5];
        
        NSLog(@"URL:%@", url);
        
        TTURLRequest* request = [TTURLRequest
                                 requestWithURL: url
                                 delegate: self];

        request.cachePolicy = cachePolicy;
        
        RemoveCallbackURLJSONResponse* response = [[RemoveCallbackURLJSONResponse alloc] init];
        request.response = response;
        TT_RELEASE_SAFELY(response);
        
        [request send];
        
        //}
    }
    
}

- (BOOL)isOutdated {
    return NO;
}

#pragma mark -
#pragma mark TTURLRequestDelegate

- (void)requestDidFinishLoad:(TTURLRequest*)request
{
    
    @try {
        
        RemoveCallbackURLJSONResponse* response = request.response;
        
        NSDictionary* data = response.rootObject;
        
        NSLog(@"response.data:%@" ,data);
        
        TTDASSERT([[data objectForKey:@"result"] isKindOfClass:[NSArray class]]);
        
        NSArray* entries = [data objectForKey:@"result"];
        
        NSDateFormatter* dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setTimeStyle:NSDateFormatterFullStyle];
        [dateFormatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.sss"];
        
        NSLog(@"asset count:%i" ,[entries count]);
        
        NSMutableArray* videos = [NSMutableArray arrayWithCapacity:[entries count]];
        
        for (NSDictionary* entry in entries) {
            Video* video = [[Video alloc] init];
            [videos addObject:video];
            TT_RELEASE_SAFELY(videos);
        }
        
        [_videoList addObjectsFromArray: videos];
        if(videos.count<_resultsPerPage){
            _finished = YES;
        }
        
        TT_RELEASE_SAFELY(dateFormatter);
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:NSLocalizedString(@"ERROROCCURED", @"Error  Occured"),[exception name]]);
        
    }
    
    [super requestDidFinishLoad:request];
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [super request:request didFailLoadWithError:error];
    
}

@end
