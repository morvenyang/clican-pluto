//
//  DownloadStatusDataSource.m
//  appletvserver
//
//  Created by zhang wei on 13-3-20.
//
//

#import "DownloadStatusDataSource.h"
#import "AppDelegate.h"

@implementation DownloadStatusDataSource
@synthesize downloadStatusModel = _downloadStatusModel;

- (id)init{
    if ((self = [super init])) {
        self.downloadStatusModel = [[DownloadStatusModel alloc] init];
    }
    return self;
}

- (void)tableViewDidLoadModel:(UITableView*)tableView {
    NSMutableArray* items = [NSMutableArray array];
    M3u8Process* m3u8Proess = [AppDele m3u8Process];
    Mp4Process* mp4Proess = [AppDele mp4Process];
    if(m3u8Proess.running){
        for(int i=0;i<[m3u8Proess.m3u8Download.m3u8DownloadLines count];i++){
            M3u8DownloadLine* line =[m3u8Proess.m3u8Download.m3u8DownloadLines objectAtIndex:i];
            NSString* text = nil;
            if(line.finished){
                text = [NSString stringWithFormat:@"%i.ts 已下载",i];
            }else{
                text = [NSString stringWithFormat:@"%i.ts 未下载",i];
            }
            TTTableTextItem* item = [TTTableTextItem itemWithText:text];
            [items addObject:item];
        }
    }else if(mp4Proess.running){
        for(int i=0;i<[mp4Proess.mp4Download.mp4DownloadPartials count];i++){
            Mp4DownloadPartial* partial =[mp4Proess.mp4Download.mp4DownloadPartials objectAtIndex:i];
            NSString* text = nil;
            if(partial.finished){
                text = [NSString stringWithFormat:@"%i.ts 已下载",i];
            }else{
                text = [NSString stringWithFormat:@"%i.ts 未下载",i];
            }
            TTTableTextItem* item = [TTTableTextItem itemWithText:text];
            [items addObject:item];
        }
    }
    if([items count]==0){
        TTTableTextItem* item = [TTTableTextItem itemWithText:@"无相关代理下载"];
        [items addObject:item];
    }
    self.items = items;
    NSLog(@"count=%i",[self.items count]);
}


- (void)dealloc {
    [super dealloc];
    TT_RELEASE_SAFELY(_downloadStatusModel);
}

- (id<TTModel>)model {
    return _downloadStatusModel;
}

///////////////////////////////////////////////////////////////////////////////////////////////////
- (NSString*)titleForLoading:(BOOL)reloading {
    return @"加载中...";
}


@end
