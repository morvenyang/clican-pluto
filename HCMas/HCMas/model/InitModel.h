//
//  InitModel.h
//  HCMas
//
//  Created by zhang wei on 14-11-21.
//  Copyright (c) 2014年 HC. All rights reserved.
//

#import <Three20Network/Three20Network.h>
@protocol InitDelegate;
@interface InitModel : TTURLRequestModel{
    id<InitDelegate> _delegate;
}
@property(nonatomic,assign) id<InitDelegate> delegate;

- (void)loadInitData;
@end


@protocol InitDelegate <NSObject>

- (void)initStart;
- (void)initSuccess:(NSArray*) projects;
- (void)initFailed:(NSError*) error message:(NSString*) message;

@end
