//
//  TudouIndexRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-3-24.
//
//

#import "TudouIndexRequestModel.h"
#import "AtvUtil.h"
@implementation TudouIndexRequestModel
    
@synthesize tudouChannel = _tudouChannel;
@synthesize keyword = _keyword;
@synthesize videoList = _videoList;
@synthesize finished = _finished;
@synthesize queryUrl = _queryUrl;

- (id)initWithTudouChannel:(TudouChannel)channel{
    if ((self = [super init])) {
        self.tudouChannel = channel;
        self.videoList = [NSMutableArray array];
    }
    return self;
}

- (void) dealloc {
    TT_RELEASE_SAFELY(_keyword);
    TT_RELEASE_SAFELY(_videoList);
    TT_RELEASE_SAFELY(_queryUrl);
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
            _page = 1;
            _finished = NO;
            [_videoList removeAllObjects];
        }
        
        
        
        if(self.queryUrl){
            NSRange range1 = [self.queryUrl rangeOfString:@"http://"];
            NSRange range2 = [self.queryUrl rangeOfString:@"2pa"];
            NSString* substring = [self.queryUrl substringWithRange:NSMakeRange(range1.location+range1.length, range2.location-range1.location-range1.length)];
            self.queryUrl = [NSString stringWithFormat:@"http://%@2pa%i.html",substring,_page];
        }else{
            if(self.tudouChannel==Tudou_DianShiJu||self.tudouChannel==Tudou_DianYing){
                self.queryUrl = [NSString stringWithFormat:@"http://www.tudou.com/cate/ach%ia-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe-2pa%i.html",self.tudouChannel,_page];
            }else{
                 self.queryUrl = [NSString stringWithFormat:@"http://www.tudou.com/cate/ich%ia-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so1pe-2pa%i.html",self.tudouChannel,_page];
            }
        }
        
        NSString* url = self.queryUrl;
        
        NSLog(@"URL:%@", url);
        
        TTURLRequest* request = [TTURLRequest
                                 requestWithURL: url
                                 delegate: self];
        
        request.cachePolicy = cachePolicy;
        
        TTURLDataResponse* response = [[TTURLDataResponse alloc] init];
        request.response = response;
        TT_RELEASE_SAFELY(response);
        
        [request send];
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
        
        TTURLDataResponse* response = request.response;
        
        NSString* content = [[[NSString alloc] initWithData:[response data] encoding:NSUTF8StringEncoding] autorelease];
        NSLog(@"content:%@" ,content);
        
        NSMutableArray* videos = [NSMutableArray array];
        
        NSArray* packs = [AtvUtil getSubValuesByTag:@"<div class=\"pack\"" startstr:@"</div>" endstr:@"</div>" tagName:@"div"];
        for (int i = 0; i < [packs count]; i++) {
            NSString* pack = [packs objectAtIndex:i];
            NSString* pic = [AtvUtil substring:pack startstr:@"<img" endstr:@">"];
            NSString* title = [AtvUtil substring:pack startstr:@"title=\"" endstr:@"\""];
            NSString* code = [AtvUtil substring:pack startstr:@"<a href=\"" endstr:@"\""];
            BOOL album = FALSE;
            if (self.tudouChannel == Tudou_DianShiJu) {
                album = TRUE;
            }
        }
        
        
                
        
        
        
        
        
        NSLog(@"asset count:%i" ,[entries count]);
        NSLog(@"video count:%i" ,[videos count]);
        [_videoList addObjectsFromArray: videos];
        if(videos.count<_resultsPerPage){
            _finished = YES;
        }
        
    }
    @catch (NSException *exception) {
        TTAlert([NSString stringWithFormat:@"错误:%@",[exception name]]);
        
    }
    
    [super requestDidFinishLoad:request];
}

- (void)request:(TTURLRequest*)request didFailLoadWithError:(NSError*)error {
    NSLog(@"request:%@ didFailLoadWithError:%@", request, error);
    [super request:request didFailLoadWithError:error];
    
}


@end
