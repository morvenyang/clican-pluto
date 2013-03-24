//
//  TudouIndexRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-3-24.
//
//

#import "TudouIndexRequestModel.h"

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
        
        TTURLJSONResponse* response = [[TTURLJSONResponse alloc] init];
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
        
        TTURLJSONResponse* response = request.response;
        
        NSDictionary* data = response.rootObject;
        
        NSLog(@"response.data:%@" ,data);
        
        
        NSMutableArray* videos = nil;
        
        
        
        
        NSArray* entries=nil;
        if(self.qqChannel==QQ_Recommand){
            entries = [data objectForKey:@"data"];
            videos = [NSMutableArray arrayWithCapacity:[entries count]];
            
            for (NSDictionary* entry in entries) {
                NSDictionary* contents = [entry objectForKey:@"contents"];
                for(NSDictionary* content in contents){
                    NSString* idType = [content objectForKey:@"id_type"];
                    if([idType isEqualToString:@"t"]){
                        continue;
                    }
                    Video* video = [[Video alloc] init];
                    video.title = [content objectForKey:@"title"];
                    video.vid = [content objectForKey:@"id"];
                    video.picUrl = [content objectForKey:@"v_pic"];
                    [videos addObject:video];
                }
            }
            
        }else if(self.qqChannel == QQ_Search){
            entries = [data objectForKey:@"list"];
            videos = [NSMutableArray arrayWithCapacity:[entries count]];
            for (NSDictionary* content in entries) {
                Video* video = [[Video alloc] init];
                video.title = [content objectForKey:@"TI"];
                video.vid = [content objectForKey:@"ID"];
                video.picUrl = [content objectForKey:@"AU"];
                if(!self.searchAlbum){
                    NSString* subTitle = [content objectForKey:@"BN"];
                    if(subTitle!=nil&&![subTitle isEqualToString:@"0"]){
                        subTitle=[@"" stringByAppendingFormat:@"第%@集",subTitle];
                    }
                    video.subTitle = subTitle;
                }
                [videos addObject:video];
            }
        }else{
            if([data objectForKey:@"cover"]!=nil){
                entries = [data objectForKey:@"cover"];
            }else{
                entries = [data objectForKey:@"video"];
            }
            videos = [NSMutableArray arrayWithCapacity:[entries count]];
            for (NSDictionary* content in entries) {
                NSString* idType = [content objectForKey:@"id_type"];
                if([idType isEqualToString:@"t"]){
                    continue;
                }
                Video* video = [[Video alloc] init];
                video.title = [content objectForKey:@"c_title"];
                video.vid = [content objectForKey:@"c_cover_id"];
                if([content objectForKey:@"c_pic"]!=nil){
                    video.picUrl = [content objectForKey:@"c_pic"];
                }else{
                    video.picUrl = [content objectForKey:@"c_pic_url"];
                }
                [videos addObject:video];
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
