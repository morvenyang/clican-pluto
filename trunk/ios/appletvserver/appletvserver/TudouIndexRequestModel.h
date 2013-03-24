//
//  TudouIndexRequestModel.h
//  appletvserver
//
//  Created by zhang wei on 13-3-24.
//
//

#import <Three20Network/Three20Network.h>
#import "Constants.h"
@interface TudouIndexRequestModel : TTURLRequestModel{
    NSString* _keyword;
    TudouChannel _tudouChannel;
    int _page;
    int _resultsPerPage;
    NSMutableArray* _videoList;
    BOOL _finished;
    NSString* _queryUrl;
}

@property (nonatomic, assign) TudouChannel tudouChannel;
@property (nonatomic, copy)     NSString*       keyword;
@property (nonatomic, retain) NSMutableArray* videoList;
@property (nonatomic, readonly) BOOL            finished;
@property (nonatomic, copy)     NSString*       queryUrl;
- (id)initWithTudouChannel:(QQChannel)channel;

@end
