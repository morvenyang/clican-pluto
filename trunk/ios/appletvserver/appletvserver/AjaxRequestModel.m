//
//  AjaxRequestModel.m
//  appletvserver
//
//  Created by zhang wei on 13-3-25.
//
//

#import "AjaxRequestModel.h"

@implementation AjaxRequestModel

- (void)load:(NSString*)cachePolicy more:(BOOL)more
{
    
    NSLog(@"load");
    
    if (!self.isLoading /*&& TTIsStringWithAnyText(_searchQuery)*/) {
        
        
        
        
        NSLog(@"URL:%@", url);
        
        TTURLRequest* request = [TTURLRequest
                                 requestWithURL: url
                                 delegate: self];
        
        request.cachePolicy = cachePolicy;
        
        RemoveCallbackURLJSONResponse* response = [[RemoveCallbackURLJSONResponse alloc] initWithCallbackName:@"QZOutputJson="];
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
