//
//  FileViewController.m
//  kxsmb project
//  https://github.com/kolyvan/kxsmb/
//
//  Created by Kolyvan on 29.03.13.
//

/*
 Copyright (c) 2013 Konstantin Bukreev All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 - Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 
 - Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/


#import "FileViewController.h"
#import "KxSMBProvider.h"
#import "AppDelegate.h"
#import "AtvUtil.h"
#import "ffmpeg.h"
@interface FileViewController ()
@end

@implementation FileViewController {
    
    UILabel         *_nameLabel;
    UILabel         *_sizeLabel;
    UILabel         *_dateLabel;
    UIButton        *_downloadButton;
    UIProgressView  *_downloadProgress;
    UILabel         *_downloadLabel;
    NSFileHandle    *_fileHandle;
    long            _downloadedBytes;
    NSDate          *_timestamp;
}

- (id)init
{
    self = [super initWithNibName:nil bundle:nil];
    if (self) {
    }
    return self;
}

- (void) loadView
{
    self.view = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]];
    self.view.backgroundColor = [UIColor whiteColor];
    
    const float W = self.view.bounds.size.width;
        
    _nameLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 10, W - 20, 30)];
    _nameLabel.font = [UIFont boldSystemFontOfSize:16];
    _nameLabel.textColor = [UIColor darkTextColor];
    _nameLabel.opaque = NO;
    _nameLabel.backgroundColor = [UIColor clearColor];
    _nameLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    
    _sizeLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 40, W - 20, 30)];
    _sizeLabel.font = [UIFont systemFontOfSize:14];
    _sizeLabel.textColor = [UIColor darkTextColor];
    _sizeLabel.opaque = NO;
    _sizeLabel.backgroundColor = [UIColor clearColor];
    _sizeLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    
    _dateLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 70, W - 20, 30)];
    _dateLabel.font = [UIFont systemFontOfSize:14];;
    _dateLabel.textColor = [UIColor darkTextColor];
    _dateLabel.opaque = NO;
    _dateLabel.backgroundColor = [UIColor clearColor];
    _dateLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
    [self.view addSubview:_nameLabel];
    [self.view addSubview:_sizeLabel];
    [self.view addSubview:_dateLabel];
    NSString *filename = _smbFile.path.lastPathComponent;
    if([filename rangeOfString:@".mkv"].location!=NSNotFound||[filename rangeOfString:@".MKV"].location!=NSNotFound||[filename rangeOfString:@".mp4"].location!=NSNotFound||[filename rangeOfString:@".MP4"].location!=NSNotFound||true){
        _downloadButton = [UIButton buttonWithType:UIButtonTypeRoundedRect];
        _downloadButton.frame = CGRectMake(10, 120, 100, 30);
        _downloadButton.titleLabel.font = [UIFont boldSystemFontOfSize:16];
        [_downloadButton setTitle:@"播放" forState:UIControlStateNormal];
        [_downloadButton setTitleColor:[UIColor blueColor] forState:UIControlStateNormal];
        [_downloadButton addTarget:self action:@selector(playAction) forControlEvents:UIControlEventTouchUpInside];
        
        _downloadLabel = [[UILabel alloc] initWithFrame:CGRectMake(10, 150, W - 20, 40)];
        _downloadLabel.font = [UIFont systemFontOfSize:14];;
        _downloadLabel.textColor = [UIColor darkTextColor];
        _downloadLabel.opaque = NO;
        _downloadLabel.backgroundColor = [UIColor clearColor];
        _downloadLabel.autoresizingMask = UIViewAutoresizingFlexibleWidth;
        _downloadLabel.numberOfLines = 2;
        
        _downloadProgress = [[UIProgressView alloc] initWithProgressViewStyle:UIProgressViewStyleDefault];
        _downloadProgress.frame = CGRectMake(10, 190, W - 20, 30);
        _downloadProgress.hidden = YES;
        [self.view addSubview:_downloadButton];
        [self.view addSubview:_downloadLabel];
        [self.view addSubview:_downloadProgress];
    }
    

  
    
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    _nameLabel.text = _smbFile.path;
    _sizeLabel.text = [NSString stringWithFormat:@"size: %ld", _smbFile.stat.size];
    _dateLabel.text = [NSString stringWithFormat:@"date: %@", _smbFile.stat.lastModified];
}

- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];    
    [self closeFiles];
}

- (void) closeFiles
{
    if (_fileHandle) {
        
        [_fileHandle closeFile];
        _fileHandle = nil;
    }
    if(_smbFile){
        [_smbFile close];
        _smbFile = nil;
    }
}

- (void) playAction
{
    NSString *filename = _smbFile.path.lastPathComponent;
    NSString* url = nil;
    NSData* data =nil;
    if([AtvUtil content:filename contains:@".mp4"]){
        url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/proxy/play.mp4?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:_smbFile.path]];
    }else if([AtvUtil content:filename contains:@".mp3"]){
        NSData* data = [_smbFile readDataToEndOfFile];
        url = [AppDele.localMp3PathPrefix stringByAppendingString:@"1.mp3"];
        [[NSFileManager defaultManager] createFileAtPath:url contents:data attributes:nil];
    }else{
        url = [NSString stringWithFormat:@"http://%@:8080/appletv/noctl/mkv/play.m3u8?url=%@",AppDele.ipAddress,[AtvUtil encodeURL:_smbFile.path]];
    }
    
    NSLog(@"play:%@",url);
    if([AtvUtil content:url startWith:@"http"]){
        self.playerViewController = [[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL URLWithString:url]];
    }else{
        self.playerViewController = [[MPMoviePlayerViewController alloc] initWithContentURL:[NSURL fileURLWithPath:url]];
    }
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(moviePlayBackDidFinish:)
                                                 name:MPMoviePlayerPlaybackDidFinishNotification object:self.playerViewController.moviePlayer];
    
    self.playerViewController.moviePlayer.controlStyle = MPMovieControlStyleFullscreen;
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    [self presentMoviePlayerViewControllerAnimated:self.playerViewController];
    [self.playerViewController.moviePlayer prepareToPlay];
    [self.playerViewController.moviePlayer setFullscreen:YES animated:YES];
    [self.playerViewController.moviePlayer play];
}

- (void)moviePlayBackDidFinish:(NSNotification*)notification {
    
    //user hit the done button
    MPMoviePlayerController *moviePlayer = [notification object];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self
                                                    name:MPMoviePlayerPlaybackDidFinishNotification
                                                  object:moviePlayer];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.navigationController popViewControllerAnimated:YES];
    transfer_code_interrupt = 1;
}

-(void) updateDownloadStatus: (id) result
{
    if ([result isKindOfClass:[NSError class]]) {
         
        NSError *error = result;
        
        [_downloadButton setTitle:@"Download" forState:UIControlStateNormal];
        _downloadLabel.text = [NSString stringWithFormat:@"failed: %@", error.localizedDescription];
        _downloadProgress.hidden = YES;        
       [self closeFiles];
        
    } else if ([result isKindOfClass:[NSData class]]) {
        
        NSData *data = result;
                
        if (data.length == 0) {
        
            [_downloadButton setTitle:@"Download" forState:UIControlStateNormal];          
            [self closeFiles];
            
        } else {
            
            NSTimeInterval time = -[_timestamp timeIntervalSinceNow];
            
            _downloadedBytes += data.length;
            _downloadProgress.progress = (float)_downloadedBytes / (float)_smbFile.stat.size;
            
            CGFloat value;
            NSString *unit;
            
            if (_downloadedBytes < 1024) {
                
                value = _downloadedBytes;
                unit = @"B";
                
            } else if (_downloadedBytes < 1048576) {
                
                value = _downloadedBytes / 1024.f;
                unit = @"KB";
                
            } else {
                
                value = _downloadedBytes / 1048576.f;
                unit = @"MB";
            }
            
            _downloadLabel.text = [NSString stringWithFormat:@"downloaded %.1f%@ (%.1f%%) %.2f%@s",
                                   value, unit,
                                   _downloadProgress.progress * 100.f,
                                   value / time, unit];
            
            if (_fileHandle) {
                
                [_fileHandle writeData:data];
                [self download];
            }
        }
    } else {
        
        NSAssert(false, @"bugcheck");        
    }
}

- (void) download
{    
    __weak __typeof(self) weakSelf = self;
    [_smbFile readDataOfLength:32768
                         block:^(id result)
    {
        FileViewController *p = weakSelf;
        if (p && p.isViewLoaded && p.view.window) {
            [p updateDownloadStatus:result];
        }
    }];
}

@end
