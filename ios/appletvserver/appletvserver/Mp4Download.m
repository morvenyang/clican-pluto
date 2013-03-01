//
//  Mp4Download.m
//  appletvserver
//
//  Created by zhang wei on 13-2-28.
//
//

#import "Mp4Download.h"
#import "Constants.h"

@implementation Mp4Download

@synthesize mp4Url = _mp4Url;
@synthesize mp4DownloadPartials = _mp4DownloadPartials;
@synthesize totalLength = _totalLength;

- (Mp4DownloadPartial*) getNextDownloadPartial{
    @synchronized(self) {
        if(_downloadIndex==0){
            _downloadIndex = 1;
        }
        if(_downloadIndex<[_mp4DownloadPartials count]){
            Mp4DownloadPartial* next=[_mp4DownloadPartials objectAtIndex:_downloadIndex];
            _downloadIndex++;
            return next;
        }else{
            return nil;
        }
    }
}
- (Mp4DownloadPartial*) getMaxFinishedDownloadPartial{
    Mp4DownloadPartial* partial = NULL;
    int start = _maxFinishedIndex;
    for(int i=start;i<[_mp4DownloadPartials count];i++){
        Mp4DownloadPartial* temp = [_mp4DownloadPartials objectAtIndex:i];
        if(temp.finished){
            partial = temp;
            _maxFinishedIndex = i;
        }else{
            break;
        }
    }
    return partial;
}

- (NSData*) getDataByStartPosition:(long) startPosition endPosition:(long) endPosition
{
    BOOL hasData = false;
    NSMutableData* data = [NSMutableData dataWithCapacity:endPosition-startPosition];
    long offsetPosition = startPosition;
    long index = offsetPosition/MP4_PARTIAL_LENGTH;
    Mp4DownloadPartial* partial = [self.mp4DownloadPartials objectAtIndex:index];
    while(true){
        if(partial.finished){
            long start = offsetPosition-partial.startPosition;
            NSLog(@"start:%ld,offset:%ld,start:%ld",start,offsetPosition,partial.startPosition);
            long length = endPosition-partial.startPosition+1;
            if(length>(partial.endPosition-partial.startPosition)){
                length= partial.endPosition-offsetPosition;
            }
            NSData* temp = [NSData dataWithContentsOfFile:partial.localPath];
            NSLog(@"read data from:%ld to:%ld",partial.startPosition+start,partial.startPosition+length);
            NSRange subDataRange = NSMakeRange(start,length);
            NSLog(@"get sub data %ld-%ld from %i-%i",start,start+length,0,[temp length]);
            temp = [temp subdataWithRange:subDataRange];
            [data appendData:temp];
            if(partial.endPosition>=endPosition||partial.endPosition==[self totalLength]){
                break;
            }else{
                index++;
                offsetPosition = partial.endPosition;
                partial = [self.mp4DownloadPartials objectAtIndex:index];
            }
        } else {
            if(hasData){
                break;
            }else{
                NSLog(@"The mp4 content %ld-%ld is still downloading.",startPosition,endPosition);
                [NSThread sleepForTimeInterval:1.0f];
            }
        }
    }
    NSLog(@"read total data from:%ld to:%ld",startPosition,startPosition+[data length]);
    return data;
}


- (void) dealloc {
    TT_RELEASE_SAFELY(_mp4DownloadPartials);
    TT_RELEASE_SAFELY(_mp4Url);
    [super dealloc];
}
@end
